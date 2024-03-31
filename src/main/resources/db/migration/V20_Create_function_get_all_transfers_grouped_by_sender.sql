CREATE OR REPLACE FUNCTION get_all_transfers_grouped_by_sender()
RETURNS TABLE (
    id_sender VARCHAR,
    registration_date TIMESTAMP,
    transfer_id VARCHAR,
    amount DOUBLE PRECISION,
    effective_date TIMESTAMP,
    reason VARCHAR,
    label VARCHAR,
    is_canceled BOOLEAN,
    ref VARCHAR,
    id_receiver VARCHAR
) AS $$
BEGIN
    RETURN QUERY 
    SELECT
        id_sender,
        registration_date,
        id,
        amount,
        effective_date,
        reason,
        label,
        is_canceled,
        ref,
        id_receiver
    FROM
        "transfer"
    ORDER BY
        id_sender, registration_date DESC;
END;
$$ LANGUAGE plpgsql;
