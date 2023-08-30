ALTER TABLE public.visits
ALTER COLUMN review TYPE INT USING review::integer;
;