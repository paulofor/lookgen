#!/usr/bin/env bash
set -euo pipefail

# Gera documentacao usando a CLI da OpenAI.
# Exemplo de uso:
#   ./tools/gen-ai-docs.sh docs.ai/styler Especificacao_1_0.txt

SRC_DIR=${1:-docs.ai}
PROMPT_FILE=${2:-prompt.txt}
OUT_FILE=${3:-generated-docs.md}

if ! command -v openai >/dev/null 2>&1; then
  echo "openai CLI not found. Instale com 'pip install openai'." >&2
  exit 1
fi

openai api completions.create -m gpt-4 -p "$(cat "$SRC_DIR/$PROMPT_FILE")" > "$OUT_FILE"
echo "Documentacao gerada em $OUT_FILE"
