(ns chat-server.handler.hello
  (:require [ring.util.response :as ring-resp]))

(defn respond-hello [request]
  (ring-resp/response {:greeting "Hello, world!"}))
