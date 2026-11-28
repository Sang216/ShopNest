-- Makes a second, big database with 200,000 products. Exactly 20 of them are a telescope. Run it with:
--   mysql -u root -p < seed/scale-200k.sql
DROP DATABASE IF EXISTS shopnest_scale;
CREATE DATABASE shopnest_scale;
USE shopnest_scale;

CREATE TABLE category LIKE productcatalog.category;
CREATE TABLE product LIKE productcatalog.product;
INSERT INTO category SELECT * FROM productcatalog.category;

SET SESSION cte_max_recursion_depth = 200000;

INSERT INTO product (id, name, description, price, image_url, category_id, state, created_at, last_updated_at)
WITH RECURSIVE n(i) AS (
    SELECT 1 UNION ALL SELECT i + 1 FROM n WHERE i < 200000
),
               picks AS (
                   SELECT i, 1 + FLOOR(RAND() * 8) AS a, IF(i MOD 10000 = 0, 13, 1 + FLOOR(RAND() * 12)) AS w, 1 + FLOOR(RAND() * 6) AS u FROM n
               ),
               cats AS (
                   SELECT id, ROW_NUMBER() OVER () AS rn FROM category
               )
SELECT /*+ NO_MERGE(picks) */
    UUID_TO_BIN(UUID()),
    CONCAT(ELT(a, 'Red', 'Blue', 'Soft', 'Smart', 'Classic', 'Compact', 'Organic', 'Wireless'), ' ',
           ELT(w, 'Phone', 'Laptop', 'Charger', 'Headphones', 'Watch', 'Backpack', 'Shoes', 'Lamp', 'Perfume', 'Lipstick', 'Mascara', 'Shampoo', 'Telescope')),
    CONCAT('A ', ELT(a, 'red', 'blue', 'soft', 'smart', 'classic', 'compact', 'organic', 'wireless'), ' ',
           ELT(w, 'phone', 'laptop', 'charger', 'headphones', 'watch', 'backpack', 'shoes', 'lamp', 'perfume', 'lipstick', 'mascara', 'shampoo', 'telescope'), ' ',
           ELT(u, 'for everyday use', 'that lasts for years', 'made for busy people', 'with a one year warranty', 'in a gift box', 'at a fair price')),
    ROUND(1 + RAND() * 199, 2),
    NULL,
    cats.id,
    'ACTIVE', NOW(6), NOW(6)
FROM picks JOIN cats ON cats.rn = 1 + (picks.i MOD 24);