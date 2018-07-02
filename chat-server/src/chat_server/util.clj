(ns chat-server.util
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :refer [transform-keys]]))

(defn ->kebab-case [v]
  (csk/->kebab-case v :separator \_))

(defn ->snake_case [v]
  (csk/->snake_case v :separator \-))

(defn transform-keys-to-kebab [m]
  (transform-keys ->kebab-case m))

(defn transform-keys-to-snake [m]
  (transform-keys ->snake_case m))
