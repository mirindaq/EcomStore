FROM node:20-alpine

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .

ARG VITE_API_BASE_URL=http://localhost:8080/api/v1
ARG VITE_WS_BASE_URL=http://localhost:8080/ws
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
ENV VITE_WS_BASE_URL=$VITE_WS_BASE_URL

RUN npm run build

EXPOSE 80

CMD ["npx", "serve", "-s", "dist", "-l", "80"]
