(ns re-frame-sample-app.views.channel
  (:require [re-frame-sample-app.views.core :refer [view]]))

(defmethod view ::view [{:keys [route-params]}]
  [:div "Channl ID: " (:channel-id route-params)])
