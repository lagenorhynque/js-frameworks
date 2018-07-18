(ns chat-server.util.instant
  (:require [cheshire.generate :refer [add-encoder]]
            [java-time :as time])
  (:import (java.time ZonedDateTime)))

;;; tagged literal `#time/inst`

(defmethod print-method ZonedDateTime
  [^ZonedDateTime dt ^java.io.Writer out]
  (.write out (str "#time/inst \"" (time/format :iso-offset-date-time dt) "\"") ))

(defmethod print-dup ZonedDateTime
  [^ZonedDateTime dt ^java.io.Writer out]
  (print-method dt out))

(defn ->time-inst [dt-str]
  (time/zoned-date-time dt-str))

;;; JSON encoding

(add-encoder
 ZonedDateTime
 (fn [^ZonedDateTime dt json-generator]
   (.writeString json-generator (time/format :iso-offset-date-time dt))))
