CREATE OR REPLACE FUNCTION account_statements(account_number_param varchar)
RETURNS TABLE (
    date timestamp,
    reference varchar,
    reason varchar,
    credit double precision,
    debit double precision,
    balance double precision
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        tr.effective_date AS date,
        tr.ref AS reference,
        tr.reason AS reason,
        CASE WHEN t.type = 'CREDIT' THEN tr.amount ELSE 0 END AS credit,
        CASE WHEN t.type = 'DEBIT' THEN tr.amount ELSE 0 END AS debit,
        bh.main_balance AS balance
    FROM
        "transfer" tr
    INNER JOIN
        "transaction" t ON tr.id = t.id_transfer
    LEFT JOIN LATERAL (
        SELECT main_balance
        FROM "balance_history" bh
        WHERE bh.id_account = tr.id_sender OR bh.id_account = tr.id_receiver
              AND bh.date <= t.date
        ORDER BY bh.date DESC
        LIMIT 1
    ) bh ON true
    INNER JOIN
        "account" a ON a.account_number = account_number_param
                      AND (tr.id_sender = a.id OR tr.id_receiver = a.id)
    WHERE
        a.account_number = account_number_param
    ORDER BY
        tr.effective_date DESC;
END;
$$ LANGUAGE plpgsql;
