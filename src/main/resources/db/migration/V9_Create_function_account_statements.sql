CREATE OR REPLACE FUNCTION account_statements(id_account_param varchar)
RETURNS TABLE (
    date timestamp,
    reference varchar,
    motif varchar,
    credit double precision,
    debit double precision,
    solde double precision
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        t.date AS date,
        tr.ref AS reference,
        tr.reason AS motif,
        CASE WHEN t.type = 'CREDIT' THEN tr.amount ELSE 0 END AS credit,
        CASE WHEN t.type = 'DEBIT' THEN tr.amount ELSE 0 END AS debit,
        bh.main_balance AS solde
    FROM
        "transfer" tr
    INNER JOIN
        "transaction" t ON tr.id = t.id_transfer
    INNER JOIN
        "balance_history" bh ON t.date >= bh.date AND bh.id_account = id_account_param
    WHERE
        t.id_account = id_account_param
    ORDER BY
        t.date DESC;
END;
$$ LANGUAGE plpgsql;
