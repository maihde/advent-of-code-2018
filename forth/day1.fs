\ One important part of FORTH is to understand how to read stack-effect comments
\ such as ( n -- n^3).  The parts before '--' are the state of the stack prior to
\ the call and the parts after '--' are the state of the stack after execution.

\ Another convention seems to be following each line with a comment in parenthesis
\ that describes the stack state. 

\ open the file as fd-in...I have no idea what the 'throw' statement does
\ but all the examples have it
s" ../dat/day/1/input.txt" r/o open-file throw Value fd-in

\ create a constant value 256
256 Constant max-line

\ allocate a line buffer for max-line bytes plus to for end-lines
Create line-buffer max-line 2 + allot

\ The built in sign? doesn't handle '+' correctly
\ so patch it here.
: patched_sign? ( addr u -- addr1 u1 flag )
    over c@ [char] + = 
    IF
    	1 /string
    THEN

    over c@ [char] - =  dup >r
    IF
	1 /string
    THEN
        r> ;

: patched_s>number? ( addr u -- d f ) \ gforth
    \ G converts string addr u into d, flag indicates success
    patched_sign? >r
    s>unumber?
    0= IF
        rdrop false
    ELSE \ no characters left, all ok
	r> ?dnegate
	true
    THEN ;

: sum-file ( addr -- d )

0 \ the accumulator is at the top of the stack

 begin
   line-buffer max-line fd-in read-line throw ( length not-eof-flag)
 while ( length )
   \ put the line-buffer on the stack and swap so it's in addr, len
   \ order like many functions expect
   line-buffer swap
   \ Convert string to number; the built in version in GForth doesn't
   \ handle leading '+' symbol so we use our own version
   patched_s>number? 
   \ the documentation claims that the return from s>number? is 'd f'
   \ but it seems to be 'd f f' where the first flag is if the number
   \ if negative or not
   \ sign?
   IF
   drop
   +
   ELSE
   \ not a number
   drop drop
   THEN
 repeat 
 drop \ when length is zero it's left on the stack, so drop it
 ;

sum-file

. \ print out the part 1 answer

\ i'm not going to do part 2 in Forth because it lacks a hash-set or equivalent
