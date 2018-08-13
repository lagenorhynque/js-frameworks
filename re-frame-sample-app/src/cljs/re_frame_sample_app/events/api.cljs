(ns re-frame-sample-app.events.api
  (:require [ajax.core :as ajax]))

(def base-url "http://localhost:8080/api")

(def api-get
  {:method :get
   :response-format (ajax/json-response-format {:keywords? true})})

(def api-post
  {:method :post
   :format (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})})

(def get-channels
  (assoc api-get
         :uri (str base-url "/channels")))

(defn get-channel [channel-id]
  (assoc api-get
         :uri (str base-url "/channels/" channel-id)))

(defn get-channel-messages [channel-id]
  (assoc api-get
         :uri (str base-url "/channels/" channel-id "/messages")))

(defn post-channel-message [channel-id message]
  (assoc api-post
         :uri (str base-url "/channels/" channel-id "/messages")
         :params message))
