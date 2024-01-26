# BUILD
FROM docker.io/oven/bun:alpine as buildstage

COPY package.json /build/

WORKDIR /build
RUN bun install

COPY components/ pages/ public/ server/ .env *.vue *.ts *.js *.json /build/
RUN bun run build

# RUN
FROM docker.io/node:alpine
COPY --from=buildstage /build/.output/ /app

WORKDIR /app

EXPOSE 3000
ENTRYPOINT node server/index.mjs

