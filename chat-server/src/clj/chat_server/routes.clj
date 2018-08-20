(ns chat-server.routes
  (:require [chat-server.handler.authentication :as authentication]
            [chat-server.handler.channels :as channels]
            [chat-server.interceptor :as interceptor]
            [integrant.core :as ig]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route :as route]))

;;; validation rules

(def validation-schemas
  (merge authentication/validations
         channels/validations))

;;; routing

(defmethod ig/init-key ::routes
  [_ {:keys [db redis]}]
  (let [common-interceptors [(body-params/body-params)
                             http/json-body
                             (interceptor/store-session redis)
                             interceptor/authenticate
                             interceptor/attach-tx-data
                             (interceptor/validate validation-schemas)
                             (interceptor/attach-database db)]
        auth-interceptors [(body-params/body-params)
                           http/json-body
                           (interceptor/store-session redis)
                           interceptor/attach-tx-data
                           (interceptor/validate validation-schemas)
                           (interceptor/attach-database db)]]
    #(route/expand-routes
      #{["/api/authentication" :get (conj common-interceptors
                                          `authentication/fetch-user)]
        ["/api/authentication/login" :post (conj auth-interceptors
                                                 `authentication/login)]
        ["/api/authentication/logout" :delete (conj auth-interceptors
                                                    `authentication/logout)]
        ["/api/channels" :get (conj common-interceptors
                                    `channels/list-channels)]
        ["/api/channels" :post (conj common-interceptors
                                     `channels/create-channel)]
        ["/api/channels/:channel-id" :get (conj common-interceptors
                                                `channels/fetch-channel)]
        ["/api/channels/:channel-id/messages" :get (conj common-interceptors
                                                         `channels/list-channel-messages)]
        ["/api/channels/:channel-id/messages" :post (conj common-interceptors
                                                          `channels/create-message)]})))
