(ns chat-server.handler.hello
  (:require [ring.util.http-response :as response]
            [struct.core :as st]))

(def validations
  {::respond-hello [[:foo st/number-str]]})

(defn respond-hello [request]
  (response/ok {:data {:greeting-from-server "Hello, world!"}}))
