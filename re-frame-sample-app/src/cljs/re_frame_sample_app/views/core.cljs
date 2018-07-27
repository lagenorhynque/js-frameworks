(ns re-frame-sample-app.views.core
  (:require [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.icons :as ic]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame-sample-app.subs :as subs]
            [re-frame.core :as re-frame]
            [stylefy.core :refer [use-style]]))

(defmulti view :handler)

(defmethod view :default [_]
  [:h1 "404 Not Found"])

(defn main-panel []
  [ui/mui-theme-provider
   {:mui-theme (get-mui-theme
                {;; TODO: 全体テーマ設定
                 ;; :palette {:text-color (color :blue600)}
                 })}
   [view @(re-frame/subscribe [::subs/current-route])]])
