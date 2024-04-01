CREATE OR REPLACE FUNCTION account_statements(account_number varchar)
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
    INNER JOIN "transaction" t ON tr.id = t.id_transfer
                            AND (t.id_account = account_number OR tr.id_sender = account_number OR tr.id_receiver = account_number)

    INNER JOIN
        "balance_history" bh ON t.date >= bh.date AND bh.id_account = account_number
    WHERE
        t.id_account = account_number
    ORDER BY
        t.date DESC;
END;
$$ LANGUAGE plpgsql;
