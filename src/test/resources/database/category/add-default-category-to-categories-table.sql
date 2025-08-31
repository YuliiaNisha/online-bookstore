DELETE FROM categories;
ALTER TABLE categories AUTO_INCREMENT = 1;
INSERT INTO categories (id, name, description, is_deleted)
VALUES
(1, 'Default category', 'Default category description', FALSE)