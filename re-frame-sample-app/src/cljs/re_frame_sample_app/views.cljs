(ns re-frame-sample-app.views
  (:require [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.icons :as ic]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame-sample-app.subs :as subs]
            [re-frame.core :as re-frame]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Hello from " @name]]))
