import { createClient } from '@supabase/supabase-js';

const supabaseUrl = 'https://dhehoatnzlbcxtlovckc.supabase.co/';  // Replace with your Supabase URL
const supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRoZWhvYXRuemxiY3h0bG92Y2tjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzEzNDIwNDIsImV4cCI6MjA0NjkxODA0Mn0.liR5-Gnb9UE3N1PpSSsevaxTfdbNUfGJepsa3nTnzVY';  // Replace with your Supabase anon key
const supabase = createClient(supabaseUrl, supabaseKey);

export default supabase;
