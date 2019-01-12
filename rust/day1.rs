use std::collections::HashSet;
use std::error::Error;
use std::fs::File;
use std::io::prelude::*;

fn compute_frequency(input: &str) -> Result<i64, Box<Error>> {
    Ok(input.trim().split("\n")
        .map(|x| {
            x.parse::<i64>()
             .expect(&format!("Error could not parse {} as i64", x))
        }).sum())
}

fn compute_duplicate_frequency(input: &str) -> Result<i64, String> {
    let mut set = HashSet::new();
    set.insert(0);
    let result = input.trim().split("\n")
        .map(|x| {
            x.parse::<i64>()
                .expect(&format!("Error could not parse {} as i64", x))
        })
        .cycle()
        .scan(0, |a, b| {
            *a += b;
            Some(*a)
        })
        .filter(|element| !set.insert(*element))
        .next();

    match result {
        Some(r) => Ok(r),
        None => Err(String::from("no dupes"))
    }
}

fn main() -> Result<(), Box<Error>> {
    let mut f = File::open("1.input").expect("file not found");
    let mut puzzle_input = String::new();
    f.read_to_string(&mut puzzle_input)
     .expect("something went wrong reading the file");

    println!("Part one answer: {}", compute_frequency(&puzzle_input)?);
    println!(
        "Part two answer: {}",
        compute_duplicate_frequency(&puzzle_input)?
    );
    Ok(())
}
