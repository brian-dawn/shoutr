(ns shoutr.test.db.core
  (:require [shoutr.db.core :as db]
            [shoutr.db.migrations :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [taoensso.timbre.tools.logging :as legacy-logging]
            [conman.core :refer [with-transaction]]
            [environ.core :refer [env]]))

(use-fixtures
  :once
  (fn [f]
    (legacy-logging/use-timbre)
    (db/connect!)
    (migrations/migrate ["migrate"])
    (f)))

(deftest test-users
  (let [uuid (db/gen-uuid)]
    (with-transaction [t-conn db/conn]
      (jdbc/db-set-rollback-only! t-conn)
      (is (= 1 (db/create-user!
                {:user_uid uuid
                 :username "Sam"
                 :email    "sam.smith@example.com"
                 :password "pass"})))
      (is (= [{:user_uid uuid
               :username "Sam"
               :email    "sam.smith@example.com"
               :password "pass"}]
             (db/get-user {:user_uid uuid}))))))
