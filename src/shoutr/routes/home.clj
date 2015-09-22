(ns shoutr.routes.home
  (:require [shoutr.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]
            [shoutr.auth :as auth]))

(defn home-page []
  (layout/render "home.html"))

(defn google [params]
  (let [id-token (:id_token params)]
    (auth/google-login id-token)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/docs" [] (ok (-> "docs/docs.md" io/resource slurp)))
  (POST "/google" {params :params} (google params)))
