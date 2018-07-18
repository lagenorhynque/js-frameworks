(ns chat-server.routes
  (:require [chat-server.handler.channels :as channels]
            [chat-server.handler.hello :as hello]
            [chat-server.interceptor :as interceptor]
            [integrant.core :as ig]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route :as route]))

;;; validation rules

(def validation-schemas
  (merge channels/validations
         hello/validations))

;;; routing

(defmethod ig/init-key ::routes
  [_ {:keys [db]}]
  (let [common-interceptors [(body-params/body-params)
                             http/json-body
                             interceptor/attach-tx-data
                             (interceptor/validate validation-schemas)
                             (interceptor/attach-database db)]]
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
