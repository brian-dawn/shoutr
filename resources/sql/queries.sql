-- name: create-user!
-- creates a new user record
INSERT INTO users
(user_uid, username, email, password)
VALUES (:user_uid, :username, :email, :password)

-- name: get-user
-- retrieve a user given the id.
SELECT * FROM users
WHERE user_uid = :user_uid

-- name: delete-user!
-- delete a user given the id
DELETE FROM users
WHERE user_uid = :user_uid
