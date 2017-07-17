(ns top.jmjoy.rbac.test.model
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [top.jmjoy.rbac.config :as config]
            [top.jmjoy.rbac.util :as util]
            [top.jmjoy.rbac.model :refer :all])
  (:import [top.jmjoy.rbac.model User Role Node]))

(defn truncate-table [table-name]
  (jdbc/execute! config/jdbc-config (format "truncate `%s`" table-name)))

(defn fixture-tables [f]
  (truncate-table "user")
  (truncate-table "role")
  (truncate-table "node")
  (truncate-table "user_role")
  (truncate-table "role_node")
  (log/info "Truncated tables.")
  (f))

(use-fixtures :each fixture-tables)

(deftest test-user-add
  (is (instance? User (user-add "test")))
  (let [{:keys [id name create-time]} (user-add "test2")]
    (is (= id 2))
    (is (= name "test2"))
    (is (<= create-time (util/current-timestamp)))))

(deftest test-role-add
  (let [role1 (role-add "role1")]
    (is (instance? Role role1))
    (is (= 1 (:id role1)))
    (is (= "role1" (:name role1)))
    (is (<= (:create-time role1) (util/current-timestamp))))

  (let [role2 (role-add "role2")]
    (is (instance? Role role2))
    (is (= 2 (:id role2)))
    (is (= "role2" (:name role2)))
    (is (<= (:create-time role2) (util/current-timestamp)))))

(deftest test-node-add
  (let [node1 (node-add "node1")]
    (is (instance? Node node1))
    (is (= 1 (:id node1)))
    (is (= "node1" (:name node1)))
    (is (<= (:create-time node1) (util/current-timestamp)))
    (is (nil? (:parent-node node1)))
    (is (= 0 (:parent_id (first (jdbc/query
                                 config/jdbc-config
                                 "select * from node where id = 1")))))

    (let [node2 (node-add "node2" node1)]
      (is (instance? Node node2))
      (is (= 2 (:id node2)))
      (is (= "node2" (:name node2)))
      (is (<= (:create-time node2) (util/current-timestamp)))
      (is (= node1 (:parent-node node2)))
      (is (= 1 (:parent_id (first (jdbc/query
                                   config/jdbc-config
                                   "select * from node where id = 2"))))))))
