# Stage 1 build app
FROM node:18
WORKDIR /app
COPY . /app
ARG API_HOSTNAME=13.81.83.209
#172.20.200.10
RUN sed -i "s/localhost/${API_HOSTNAME}/g" /app/src/api/axios.js
RUN npm install
ENTRYPOINT ["npm", "start"]
