(ns top.jmjoy.rbac.protocol)

(defprotocol Operation
  (id [this])
  (parent-id [this])
  (level [this]))

(defprotocol Role
  (id [this])
  (add-operation [this ^Operation operation])
  (remove-operation [this ^Operation operation]))

(defprotocol User
  (id [this])
  (add-role [this ^Role role])
  (remove-role [this ^Role role]))
