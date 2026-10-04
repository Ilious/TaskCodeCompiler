FROM debian:bookworm-slim

RUN apt-get update && apt-get install -y \
    gcc \
    clang \
    python3 \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

ENV PYTHON_BIN=/usr/bin/python3
ENV CLANG_BIN=/usr/bin/clang
ENV GCC_BIN=/usr/bin/gcc

WORKDIR /code

CMD ["sh"]