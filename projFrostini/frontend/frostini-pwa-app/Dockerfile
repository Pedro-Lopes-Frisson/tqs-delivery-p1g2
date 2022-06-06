# Stage 1 build app
FROM node:18 as build
WORKDIR /app
COPY . /app
# Replace localhost with correct hostname
ARG API_HOSTNAME=frostiniSpring
RUN sed -i "s/localhost/${API_HOSTNAME}/g" src/api/axios.js
RUN npm install
RUN npm run build


# stage 2 run in nginx
FROM nginx:stable-alpine

COPY  --from=build /app/build/ /usr/share/nginx/html
