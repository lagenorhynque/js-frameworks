(ns re-frame-sample-app.views.home
  (:require [re-frame-sample-app.views.core :refer [view]]))

(defmethod view ::view [_]
  [:h1 "Sample Application"])
