(ns shoutr.auth
  (:require [org.httpkit.client :as http]
            [taoensso.timbre :as timbre]))

(defn google-login [id-token]
  (let [{:keys [status error] :as resp} @(http/get "https://www.googleapis.com/oauth2/v3/tokeninfo" {:query-params {"id_token" id-token}})]
    (if (or (not= status 200) error)
      (do (timbre/error "Token info validation failed, exception: " error) {:status 403})
      (do (timbre/info "Token info validation success: " status) {:status 200}))))
