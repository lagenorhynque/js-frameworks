(ns chat-server.routes
  (:require [chat-server.handler.core :as handler]
            [chat-server.handler.hello :as hello]
            [chat-server.handler.channels :as channels]
            [integrant.core :as ig]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route :as route]))

;;; validation rules

(def validation-schemas
  (merge hello/validations
         channels/validations))

;;; routing

(defmethod ig/init-key ::routes
  [_ {:keys [db]}]
  (let [common-interceptors [(body-params/body-params)
                             http/json-body
                             handler/attach-tx-data
                             (handler/validate validation-schemas)
                             (handler/attach-database db)]]
    #(route/expand-routes
      #{["/api/greet" :any (conj common-interceptors
                                 `hello/respond-hello)]
        ["/api/channels" :get (conj common-interceptors
                                    `channels/list-channels)]
        ["/api/channels" :post (conj common-interceptors
                                     `channels/create-channel)]
        ["/api/channels/:channel-id/messages" :get (conj common-interceptors
                                                         `channels/list-channel-messages)]
        ["/api/channels/:channel-id/messages" :post (conj common-interceptors
                                                          `channels/create-message)]})))
