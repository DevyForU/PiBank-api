CREATE OR REPLACE FUNCTION aggregate_amount_by_category(start_date DATE, end_date DATE)
RETURNS TABLE (
    category_name VARCHAR,
    total_amount DOUBLE PRECISION
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.name AS category_name,
        SUM(tr.amount) AS total_amount
    FROM
        "transaction" t
    JOIN
        "transfer" tr ON t.id_transfer = tr.id
    JOIN
        "category" c ON t.id_categorize = c.id
    WHERE
        t.date >= start_date AND t.date < end_date
    GROUP BY
        c.name;
END;
$$ LANGUAGE plpgsql;
