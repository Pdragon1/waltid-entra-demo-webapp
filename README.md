# Entra Demo Frontend & Backend

## Setup locally

```bash
# npm
npm install

# pnpm
pnpm install

# yarn
yarn install

# bun         <---
bun install # <---
```

## Development Server

Start the development server on `http://localhost:3000`:

```bash
# npm
npm run dev

# pnpm
pnpm run dev

# yarn
yarn dev

# bun
bun dev # <---
```

## Production

Build the application for production:

```bash
# npm
npm run build

# pnpm
pnpm run build

# yarn
yarn build

# bun
bun run build # <---
```

Locally preview production build:

```bash
# npm
npm run preview

# pnpm
pnpm run preview

# yarn
yarn preview

# bun
bun run preview
```

## Docker
### Frontend
```bash
docker build --progress=plain -f Dockerfile -t waltid/entra-demo-webapp-fronte
nd  .
```

### Backend

```bash
cd backend
docker build --progress plain -f Dockerfile -t waltid/entra-demo-webap
p-backend .
```

```bash
docker run -p 7777:7777 -v ./demo.conf:/backend/demo.conf waltid/entra-demo-webapp-backend
```
