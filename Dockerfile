# BUILD
FROM docker.io/oven/bun:alpine as buildstage

COPY package.json /build/

WORKDIR /build
RUN bun install

COPY . /build/
RUN bun run build

# RUN
FROM docker.io/oven/bun:alpine
COPY --from=buildstage /build/.output/ /app

WORKDIR /app

USER bun
EXPOSE 3000/tcp
ENTRYPOINT [ "bun", "run", "server/index.mjs" ]

