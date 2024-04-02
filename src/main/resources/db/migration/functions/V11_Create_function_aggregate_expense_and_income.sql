 CREATE OR REPLACE FUNCTION aggregate_expenses_and_income(start_date DATE, end_date DATE)
RETURNS TABLE (
    year INT,
    month INT,
    total_expenses DOUBLE PRECISION,
    total_income DOUBLE PRECISION
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        EXTRACT(YEAR FROM t.date) AS year,
        EXTRACT(MONTH FROM t.date) AS month,
        SUM(CASE WHEN c.type = 'DEBIT' THEN tr.amount ELSE 0 END) AS total_expenses,
        SUM(CASE WHEN c.type = 'CREDIT' THEN tr.amount ELSE 0 END) AS total_income
    FROM
        "transaction" t
    JOIN
        "transfer" tr ON t.id_transfer = tr.id
    JOIN
        "category" c ON t.id_categorize = c.id
    WHERE
        t.date >= start_date AND t.date < end_date
    GROUP BY
        year, month;
END;
$$ LANGUAGE plpgsql;
