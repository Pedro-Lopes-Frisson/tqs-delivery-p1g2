# Stage 1 build app
FROM node:18 as build
WORKDIR /app
COPY . /app
# Replace localhost with correct hostname

#ARG API_HOSTNAME=13.81.83.209
ARG API_HOSTNAME=192.168.160.223
#172.20.202.10
RUN sed -i "s/localhost/${API_HOSTNAME}/g" /app/src/api/axios.js
CMD cat src/api/axios.js
RUN npm install
RUN npm run build


# stage 2 run in nginx
FROM nginx:stable-alpine

COPY  --from=build /app/src/api/axios.js /axios.js
RUN cat /axios.js
COPY  --from=build /app/build/ /usr/share/nginx/html
