# Configurando um Bucket no Google Cloud Storage

Este passo a passo explica como criar e preparar um bucket do Google Cloud (GCP) para armazenar imagens geradas pelo ChatGPT. É necessário possuir uma conta no GCP com faturamento habilitado.

## 1. Criar projeto e habilitar o serviço

1. Acesse o [console do Google Cloud](https://console.cloud.google.com/).
2. Se ainda não houver um projeto, clique em **Select a project** e depois em **New Project** para criar um novo.
3. No menu lateral, acesse **APIs & Services > Library** e habilite a **Cloud Storage API** para o projeto.

## 2. Criar o bucket

1. No menu principal, vá para **Storage > Cloud Storage > Buckets**.
2. Clique em **Create** para iniciar a criação do bucket.
3. Defina um nome único global para o bucket, escolha a região mais apropriada (ex.: `us-central1`) e mantenha o armazenamento padrão (Standard) caso não haja requisito específico.
4. Conclua a criação mantendo as demais configurações padrão, a menos que precise de controle de acesso mais restritivo.

## 3. Criar uma conta de serviço

1. Acesse **IAM & Admin > Service Accounts** e clique em **Create Service Account**.
2. Informe um nome (por exemplo, `bucket-uploader`) e conclua.
3. Na etapa "Grant access", adicione a função **Storage Object Admin** para permitir leitura e escrita no bucket.
4. Finalize a criação da conta de serviço.

## 4. Gerar a chave JSON

1. Na lista de contas de serviço, encontre a conta criada e clique em **Manage keys**.
2. Em **Add key**, selecione **Create new key** do tipo **JSON** e faça o download do arquivo.
3. Guarde este arquivo em local seguro — ele será utilizado pela aplicação para autenticar no GCP.

## 5. Configurar a aplicação

1. Copie o arquivo JSON para o ambiente onde o ChatGPT (ou serviço que gera a imagem) será executado.
2. Defina a variável de ambiente `GOOGLE_APPLICATION_CREDENTIALS` apontando para o caminho desse arquivo.
3. Opcionalmente, defina também o nome do bucket em uma variável (por exemplo, `GCP_BUCKET_NAME`) para facilitar a configuração.

## 6. Testar o envio de imagens

Utilize a biblioteca oficial do Google Cloud Storage para a linguagem de programação do projeto (por exemplo, o cliente [Java](https://cloud.google.com/java/docs/reference/google-cloud-storage/latest) ou [Python](https://cloud.google.com/python/docs/reference/storage/latest)). A seguir, um exemplo em Python:

```python
from google.cloud import storage

# Nome do bucket definido em GCP
bucket_name = 'seu-bucket'
# Caminho local da imagem a ser enviada
file_path = 'imagem.png'

client = storage.Client()
bucket = client.bucket(bucket_name)
blob = bucket.blob('imagem.png')
blob.upload_from_filename(file_path)
print('Imagem enviada para o bucket.')
```

Depois de testar o upload manualmente, integre esse passo ao fluxo de geração de imagens para que cada arquivo seja enviado ao bucket automaticamente.
