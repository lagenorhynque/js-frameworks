FROM node:8.11.3-alpine as build-stage

# Angular app

WORKDIR /usr/src/app

COPY ["package.json", "package-lock.json", "./"]

RUN npm i npm@latest -g && \
    npm install

COPY . .

RUN npm run build --prod

# ----------

FROM nginx:1.15.2-alpine

# Nginx

RUN rm -rf /usr/share/nginx/html/*

COPY --from=build-stage /usr/src/app/dist/angular-sample-app /usr/share/nginx/html

COPY ./nginx /etc/nginx/conf.d

CMD ["nginx", "-g", "daemon off;"]
