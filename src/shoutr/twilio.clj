(ns shoutr.twilio
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]))

;;######################################
;;# Helpers - visible for testing
;;######################################

(def ^:dynamic *sid*   "")
(def ^:dynamic *token* "")

(def api-base "https://api.twilio.com/2010-04-01")

(defn build-url [resource]
  (format "%s/Accounts/%s/%s.json"
          api-base
          *sid*
          resource))

(defn request [f resource params]
  (f
   (build-url resource)
   {:basic-auth [*sid* *token*]
    :accept :json
    :form-params params}))

(defn text-builder
  [http]
  (fn [to from body] (request http "Messages" {:To to :From from :Body body})))

(defn available-numbers-builder [http]
  (fn []
    (map #(get % "phone_number")
         (-> (request http "IncomingPhoneNumbers" {})
             deref
             :body
             json/read-str
             (get "incoming_phone_numbers")))))

;;######################################
;;# Public API
;;######################################

(defmacro with-auth
  "Wrap any twilio related API calls inside this macro so they can have their
  Twilio authentication set.
  Parameters:
  * account-sid - String: Twilio account sid
  * auth_token  - String: Twilio auth token
  * body                : The twilio code to execute.
  Example: (with-auth my-sid my-auth-token
             (text my-to my-from my-body))"
  [account_sid auth_token & body]
  `(binding [*sid* ~account_sid
             *token* ~auth_token]
     (do ~@body)))

(def text
  "Parameters:
  * to   - String: A phone-number to send an SMS to
  * from - String: A phone-number we own on twilio to perform the sending
  * body - String: The SMS body"
  (text-builder http/post))

(def available-numbers
  "Returns - [String]: Returns phone-numbers available to us from Twilio."
  (available-numbers-builder http/get))

