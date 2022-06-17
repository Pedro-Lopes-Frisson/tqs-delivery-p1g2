# Stage 1 build app
FROM node:18 as build
WORKDIR /app
COPY . /app
ARG API_HOSTNAME=172.20.0.5



RUN npm install
RUN npm run build


# stage 2 run in nginx
FROM nginx:stable-alpine

COPY  --from=build /app/build/ /usr/share/nginx/html
