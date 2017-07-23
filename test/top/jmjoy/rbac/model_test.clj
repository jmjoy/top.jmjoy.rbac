(ns top.jmjoy.rbac.model-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :as log]
            [top.jmjoy.rbac.db :as db]
            [top.jmjoy.rbac.model :refer :all]
            [top.jmjoy.rbac.util :as util])
  (:import [top.jmjoy.rbac.model Node Role User]))

(defn fixture-tables [f]
  "清空表数据。"
  (db/truncate! "user")
  (db/truncate! "role")
  (db/truncate! "node")
  (db/truncate! "user_role")
  (db/truncate! "role_node")
  (log/info "Truncated tables.")
  (f))

(use-fixtures :each fixture-tables)

(defn get-node-insert-sql [id]
  ["select * from node where id = ? " id])

(deftest test-insert
  (testing "Test insert user."
    (let [id (insert (map->User {:name "tester"}))]
      (is (= 1 id))
      (let [{:keys [id name create_time]}
            (db/query-one ["select * from user where id = ?" 1])]
        (is (= 1 id))
        (is (= "tester" name))
        (is (>= (util/current-timestamp) create_time))))

    ;; test `id` auto increment
    (is (= 2 (insert (map->User {:name "tester1"})))))


  (testing "Test insert role."
    (let [id (insert (map->Role {:name "tester"}))]
      (is (= 1 id))
      (let [{:keys [id name create_time]}
            (db/query-one ["select * from role where id = ?" 1])]
        (is (= 1 id))
        (is (= "tester" name))
        (is (>= (util/current-timestamp) create_time)))))


  (testing "Test insert node."
    (let [id (insert (map->Node {:name "tester"}))]
      (is (= 1 id))
      (let [{:keys [id name create_time parent_id]}
            (db/query-one (get-node-insert-sql 1))]
        (is (= 1 id))
        (is (= "tester" name))
        (is (>= (util/current-timestamp) create_time))
        (is (= 0 parent_id))))

    (insert (map->Node {:name "tester1" :parent-node 1}))
    (let [{:keys [name parent_id]} (db/query-one (get-node-insert-sql 2))]
      (is (= "tester1" name))
      (is (= 1 parent_id)))

    (insert (map->Node {:name "tester2" :parent-node (map->Node {:id 999})}))
    (let [{:keys [name parent_id]} (db/query-one (get-node-insert-sql 3))]
      (is (= "tester2" name))
      (is (= 999 parent_id)))))
