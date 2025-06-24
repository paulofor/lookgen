package com.lookgen.styler.service;

import com.lookgen.styler.domain.Cliente;
import com.lookgen.styler.domain.FotoItem;
import com.lookgen.styler.domain.Estilo;
import com.lookgen.styler.repo.ClienteRepository;
import com.lookgen.styler.repo.FotoItemRepo;
import com.lookgen.styler.repo.EstiloRepo;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ClienteScheduler {

    private static final Logger log = LoggerFactory.getLogger(ClienteScheduler.class);

    private final ClienteRepository clienteRepo;
    private final FotoItemRepo fotoItemRepo;
    private final EstiloRepo estiloRepo;
    private final OpenAiClient openAi;
    private final MeterRegistry meterRegistry;

    public ClienteScheduler(ClienteRepository clienteRepo, FotoItemRepo fotoItemRepo,
                            EstiloRepo estiloRepo, OpenAiClient openAi,
                            MeterRegistry meterRegistry) {
        this.clienteRepo = clienteRepo;
        this.fotoItemRepo = fotoItemRepo;
        this.estiloRepo = estiloRepo;
        this.openAi = openAi;
        this.meterRegistry = meterRegistry;
    }

    @Scheduled(fixedDelayString = "${styler.poll-ms}")
    @Transactional
    public void tick() {
        List<Cliente> clientes = clienteRepo.findPending(5);
        clientes.forEach(this::processCliente);
    }

    private void processCliente(Cliente cli) {
        Timer.Sample timer = Timer.start(meterRegistry);
        try {
            List<FotoItem> fotos = fotoItemRepo.findByClienteIdOrderByDataUpload(cli.getId());
            if (fotos.isEmpty()) {
                throw new IllegalStateException("No photos for client");
            }
            List<String> urls = fotos.stream()
                    .sorted(Comparator.comparing(FotoItem::getDataUpload))
                    .map(FotoItem::getUrl)
                    .filter(Objects::nonNull)
                    .toList();
            Estilo estilo = estiloRepo.findFirstByClienteId(cli.getId());
            String style = estilo == null ? null : estilo.getNome();
            OpenAiClient.Response result = openAi.createSketch(urls, style);
            if (estilo == null) {
                estilo = new Estilo();
                estilo.setClienteId(cli.getId());
                estilo.setNome("Sugerido");
            }
            estilo.setDescricao(result.getContent());
            estiloRepo.save(estilo);
            cli.setPrecisaProcessamento(false);
            meterRegistry.counter("cliente_jobs_total", "state", "done").increment();
        } catch (Exception ex) {
            log.warn("Failed to process client {}", cli.getId(), ex);
            cli.setPrecisaProcessamento(false);
            meterRegistry.counter("cliente_jobs_total", "state", "error").increment();
        } finally {
            clienteRepo.save(cli);
            timer.stop(meterRegistry.timer("cliente_latency_seconds"));
        }
    }
}
