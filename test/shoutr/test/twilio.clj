(ns shoutr.test.twilio
  (:require [clojure.test :refer :all]
            [shoutr.twilio :refer :all]))

(defn http-mock [url req]
  {:url url
   :req req})

(def sample-response-str "{\"incoming_phone_numbers\": [{\"phone_number\": \"555-5555\"}]}")

(deftest testing-twilio

  (testing "with-auth - verify that *sid* and *token* get set."
    (with-auth "foo" "bar"
      (is (= *sid* "foo"))
      (is (= *token* "bar"))))

  (testing "build-url"
    (with-auth "sid" "token"
      (let [url (build-url "resource")]
        (is (= "https://api.twilio.com/2010-04-01/Accounts/sid/resource.json" url)))))

  (testing "request"
    (with-auth "sid" "token"
        (let [expected-url (build-url "resource")
                results      (request http-mock "resource" "params")
                result-url   (:url results)
                result-req   (:req results)]
            (is (= expected-url result-url))
            (is (= result-req {:basic-auth ["sid" "token"]
                                :accept :json
                                :form-params "params"})))))

  (testing "text"
    (with-auth "sid" "token"
      (let [results ((text-builder http-mock) "To" "From" "Body")
            expected {:url (build-url "Messages")
                      :req {:basic-auth ["sid" "token"]
                            :accept :json
                            :form-params {:To   "To"
                                          :From "From"
                                          :Body "Body"}}}]
        (is (= expected results)))))

  (testing "available-numbers"
    (with-auth "sid" "token"
      (let [http-mock-typical-resonse (fn [& args] (atom {:body sample-response-str}))
            results ((available-numbers-builder http-mock-typical-resonse))
            expected ["555-5555"]]
        (is (= expected results))))))

