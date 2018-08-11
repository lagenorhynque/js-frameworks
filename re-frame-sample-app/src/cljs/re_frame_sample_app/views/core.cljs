(ns re-frame-sample-app.views.core
  (:require [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.icons :as ic]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame-sample-app.subs :as subs]
            [re-frame-sample-app.views.channel-list :refer [channel-list]]
            [re-frame.core :as re-frame]
            [stylefy.core :refer [use-style]]))

(defmulti view :handler)

(defmethod view :default [_]
  [:h1 "404 Not Found"])

(def main-style
  {:margin "1rem 0 1rem 16rem"})

(defn main-panel []
  [ui/mui-theme-provider
   {:mui-theme (get-mui-theme)}
   [:div
    [channel-list]
    [:main (use-style main-style)
     [ui/paper
      [view @(re-frame/subscribe [::subs/current-route])]]]]])
