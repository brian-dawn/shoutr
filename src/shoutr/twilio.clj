(ns shoutr.twilio
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]))

;;######################################
;;# Helpers - visible for testing
;;######################################

;; Used for authentication, gets set by `with-auth'.
(def ^:dynamic *sid*   "")
(def ^:dynamic *token* "")

(def api-base "https://api.twilio.com/2010-04-01")

(defn build-url
  "Builds a twilio formatted URL.
  Parameters:
  * resource - String: The twilio resouce to hit."
  [resource]
  (format "%s/Accounts/%s/%s.json"
          api-base
          *sid*
          resource))

(defn make-request
  "Performs an HTTP request against twilio. Sets up authentication based
  on *sid* and *token*, returns the response.
  Parameters:
  * request-method - Function: The function to perform the HTTP request.
  * resource       - String  : The Twilio resource to hit.
  * params         - Hash    : Params for the HTTP request."
  [request-method resource params]
  (request-method (build-url resource) {:basic-auth [*sid* *token*]
                                        :accept :json
                                        :form-params params}))

(defn text-builder
  "Returns a function that can send an SMS to a number.
  Parameters:
  * http-post - Function: The HTTP client POST function to perform the request with."
  [http-post]
  (fn [to from body] (make-request http-post "Messages" {:To to :From from :Body body})))

(defn available-numbers-builder
  "Returns a function that can retrieve all the available Twilio numbers we have.
  Parameters:
  * http-get - Function: The HTTP client GET function to perform the request with."
  [http-get]
  (fn []
    (map #(get % "phone_number")
         (-> (make-request http-get "IncomingPhoneNumbers" {})
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

