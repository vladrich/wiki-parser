/* Generated By:JJTree&JavaCC: Do not edit this line. WikiParserTokenManager.java */
package org.wyona.wiki;
import java.util.Iterator;
import java.util.Set;

public class WikiParserTokenManager implements WikiParserConstants
{
  public static  java.io.PrintStream debugStream = System.out;
  public static  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private static final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private static final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
static private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
static private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 33:
         return jjStopAtPos(0, 27);
      case 39:
         return jjMoveStringLiteralDfa1_0(0x4L);
      case 42:
         return jjStopAtPos(0, 19);
      case 45:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 47:
         return jjMoveStringLiteralDfa1_0(0x2000000000L);
      case 94:
         return jjMoveStringLiteralDfa1_0(0x8L);
      case 95:
         return jjMoveStringLiteralDfa1_0(0x2L);
      case 123:
         return jjMoveStringLiteralDfa1_0(0x400000000L);
      case 124:
         return jjStopAtPos(0, 11);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
static private final int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 39:
         if ((active0 & 0x4L) != 0L)
            return jjStopAtPos(1, 2);
         break;
      case 45:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      case 47:
         if ((active0 & 0x2000000000L) != 0L)
            return jjStopAtPos(1, 37);
         break;
      case 94:
         if ((active0 & 0x8L) != 0L)
            return jjStopAtPos(1, 3);
         break;
      case 95:
         if ((active0 & 0x2L) != 0L)
            return jjStopAtPos(1, 1);
         break;
      case 123:
         if ((active0 & 0x400000000L) != 0L)
            return jjStopAtPos(1, 34);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
static private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 45:
         if ((active0 & 0x400L) != 0L)
            return jjStopAtPos(2, 10);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
static private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
static private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
static private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
static private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
static private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
static private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 3;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x2400L & l) != 0L)
                  {
                     if (kind > 39)
                        kind = 39;
                  }
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 1:
                  if (curChar == 10 && kind > 39)
                     kind = 39;
                  break;
               case 2:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static private final int jjMoveStringLiteralDfa0_1()
{
   return jjMoveNfa_1(0, 0);
}
static private final int jjMoveNfa_1(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 11;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x2400L & l) != 0L)
                  {
                     if (kind > 4)
                        kind = 4;
                  }
                  else if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 9;
                  else if (curChar == 39)
                     jjstateSet[jjnewStateCnt++] = 5;
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 1:
                  if (curChar == 10 && kind > 4)
                     kind = 4;
                  break;
               case 2:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 5:
                  if (curChar == 39 && kind > 6)
                     kind = 6;
                  break;
               case 6:
                  if (curChar == 39)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 9:
                  if (curChar == 47 && kind > 8)
                     kind = 8;
                  break;
               case 10:
                  if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 9;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (curChar == 94)
                     jjstateSet[jjnewStateCnt++] = 7;
                  else if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 3:
                  if (curChar == 95 && kind > 5)
                     kind = 5;
                  break;
               case 4:
                  if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 7:
                  if (curChar == 94 && kind > 7)
                     kind = 7;
                  break;
               case 8:
                  if (curChar == 94)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 11 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static private final int jjMoveStringLiteralDfa0_5()
{
   switch(curChar)
   {
      case 125:
         return jjMoveStringLiteralDfa1_5(0x800000000L);
      default :
         return 1;
   }
}
static private final int jjMoveStringLiteralDfa1_5(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      return 1;
   }
   switch(curChar)
   {
      case 125:
         if ((active0 & 0x800000000L) != 0L)
            return jjStopAtPos(1, 35);
         break;
      default :
         return 2;
   }
   return 2;
}
private static final int jjStopStringLiteralDfa_2(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private static final int jjStartNfa_2(int pos, long active0)
{
   return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
}
static private final int jjStartNfaWithStates_2(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_2(state, pos + 1);
}
static private final int jjMoveStringLiteralDfa0_2()
{
   switch(curChar)
   {
      case 124:
         return jjStopAtPos(0, 13);
      default :
         return jjMoveNfa_2(0, 0);
   }
}
static private final int jjMoveNfa_2(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 11;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x2400L & l) != 0L)
                  {
                     if (kind > 12)
                        kind = 12;
                  }
                  else if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 9;
                  else if (curChar == 39)
                     jjstateSet[jjnewStateCnt++] = 5;
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 1:
                  if (curChar == 10 && kind > 12)
                     kind = 12;
                  break;
               case 2:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 5:
                  if (curChar == 39 && kind > 15)
                     kind = 15;
                  break;
               case 6:
                  if (curChar == 39)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 9:
                  if (curChar == 47 && kind > 17)
                     kind = 17;
                  break;
               case 10:
                  if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 9;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (curChar == 94)
                     jjstateSet[jjnewStateCnt++] = 7;
                  else if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 3:
                  if (curChar == 95 && kind > 14)
                     kind = 14;
                  break;
               case 4:
                  if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 7:
                  if (curChar == 94 && kind > 16)
                     kind = 16;
                  break;
               case 8:
                  if (curChar == 94)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 11 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static private final int jjMoveStringLiteralDfa0_3()
{
   return jjMoveNfa_3(0, 0);
}
static private final int jjMoveNfa_3(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 12;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x2400L & l) != 0L)
                  {
                     if (kind > 20)
                        kind = 20;
                  }
                  else if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 10;
                  else if (curChar == 39)
                     jjstateSet[jjnewStateCnt++] = 6;
                  else if (curChar == 42)
                  {
                     if (kind > 21)
                        kind = 21;
                  }
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 1:
                  if (curChar == 10 && kind > 20)
                     kind = 20;
                  break;
               case 2:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 3:
                  if (curChar == 42)
                     kind = 21;
                  break;
               case 6:
                  if (curChar == 39 && kind > 23)
                     kind = 23;
                  break;
               case 7:
                  if (curChar == 39)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 10:
                  if (curChar == 47 && kind > 25)
                     kind = 25;
                  break;
               case 11:
                  if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 10;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (curChar == 94)
                     jjstateSet[jjnewStateCnt++] = 8;
                  else if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 4:
                  if (curChar == 95 && kind > 22)
                     kind = 22;
                  break;
               case 5:
                  if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 8:
                  if (curChar == 94 && kind > 24)
                     kind = 24;
                  break;
               case 9:
                  if (curChar == 94)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 12 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static private final int jjMoveStringLiteralDfa0_4()
{
   return jjMoveNfa_4(0, 0);
}
static private final int jjMoveNfa_4(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 11;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x2400L & l) != 0L)
                  {
                     if (kind > 28)
                        kind = 28;
                  }
                  else if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 9;
                  else if (curChar == 39)
                     jjstateSet[jjnewStateCnt++] = 5;
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 1:
                  if (curChar == 10 && kind > 28)
                     kind = 28;
                  break;
               case 2:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 5:
                  if (curChar == 39 && kind > 30)
                     kind = 30;
                  break;
               case 6:
                  if (curChar == 39)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 9:
                  if (curChar == 47 && kind > 32)
                     kind = 32;
                  break;
               case 10:
                  if (curChar == 47)
                     jjstateSet[jjnewStateCnt++] = 9;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (curChar == 94)
                     jjstateSet[jjnewStateCnt++] = 7;
                  else if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 3:
                  if (curChar == 95 && kind > 29)
                     kind = 29;
                  break;
               case 4:
                  if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 7:
                  if (curChar == 94 && kind > 31)
                     kind = 31;
                  break;
               case 8:
                  if (curChar == 94)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 11 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
};
public static final String[] jjstrLiteralImages = {
"", "\137\137", "\47\47", "\136\136", null, null, null, null, null, null, 
"\55\55\55", "\174", null, "\174", null, null, null, null, null, "\52", null, null, null, 
null, null, null, null, "\41", null, null, null, null, null, null, "\173\173", 
"\175\175", null, "\57\57", null, null, null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
   "DEFAULT_NOTAGS", 
   "IN_TABLE", 
   "IN_LIST", 
   "IN_TITLE", 
   "IN_PLAIN", 
};
public static final int[] jjnewLexState = {
   -1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, 2, 0, -1, -1, -1, -1, -1, -1, 3, 0, -1, -1, -1, -1, 
   -1, -1, 4, 0, -1, -1, -1, -1, -1, 5, 0, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0x1bfffffffffL, 
};
static final long[] jjtoSkip = {
   0x4000000000L, 
};
static protected SimpleCharStream input_stream;
static private final int[] jjrounds = new int[12];
static private final int[] jjstateSet = new int[24];
static StringBuffer image;
static int jjimageLen;
static int lengthOfMatch;
static protected char curChar;
public WikiParserTokenManager(SimpleCharStream stream)
{
   if (input_stream != null)
      throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);
   input_stream = stream;
}
public WikiParserTokenManager(SimpleCharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
static public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
static private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 12; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
static public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
static public void SwitchTo(int lexState)
{
   if (lexState >= 6 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

static protected Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

static int curLexState = 0;
static int defaultLexState = 0;
static int jjnewStateCnt;
static int jjround;
static int jjmatchedPos;
static int jjmatchedKind;

public static Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }
   image = null;
   jjimageLen = 0;

   switch(curLexState)
   {
     case 0:
       try { input_stream.backup(0);
          while (curChar <= 9 && (0x200L & (1L << curChar)) != 0L)
             curChar = input_stream.BeginToken();
       }
       catch (java.io.IOException e1) { continue EOFLoop; }
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_0();
       if (jjmatchedPos == 0 && jjmatchedKind > 40)
       {
          jjmatchedKind = 40;
       }
       break;
     case 1:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_1();
       if (jjmatchedPos == 0 && jjmatchedKind > 9)
       {
          jjmatchedKind = 9;
       }
       break;
     case 2:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_2();
       if (jjmatchedPos == 0 && jjmatchedKind > 18)
       {
          jjmatchedKind = 18;
       }
       break;
     case 3:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_3();
       if (jjmatchedPos == 0 && jjmatchedKind > 26)
       {
          jjmatchedKind = 26;
       }
       break;
     case 4:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_4();
       if (jjmatchedPos == 0 && jjmatchedKind > 33)
       {
          jjmatchedKind = 33;
       }
       break;
     case 5:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_5();
       if (jjmatchedPos == 0 && jjmatchedKind > 36)
       {
          jjmatchedKind = 36;
       }
       break;
   }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
           TokenLexicalActions(matchedToken);
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else
        {
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
           continue EOFLoop;
        }
     }
     int error_line = input_stream.getEndLine();
     int error_column = input_stream.getEndColumn();
     String error_after = null;
     boolean EOFSeen = false;
     try { input_stream.readChar(); input_stream.backup(1); }
     catch (java.io.IOException e1) {
        EOFSeen = true;
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
        if (curChar == '\n' || curChar == '\r') {
           error_line++;
           error_column = 0;
        }
        else
           error_column++;
     }
     if (!EOFSeen) {
        input_stream.backup(1);
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
     }
     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

static void TokenLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      case 1 :
        if (image == null)
            image = new StringBuffer(jjstrLiteralImages[1]);
         else
            image.append(jjstrLiteralImages[1]);
                       System.out.println("going Notags");
         break;
      case 2 :
        if (image == null)
            image = new StringBuffer(jjstrLiteralImages[2]);
         else
            image.append(jjstrLiteralImages[2]);
                         System.out.println("going Notags");
         break;
      case 3 :
        if (image == null)
            image = new StringBuffer(jjstrLiteralImages[3]);
         else
            image.append(jjstrLiteralImages[3]);
                            System.out.println("going Notags");
         break;
      case 9 :
        if (image == null)
            image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
         else
            image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
                                 System.out.println("NC");
         break;
      case 11 :
        if (image == null)
            image = new StringBuffer(jjstrLiteralImages[11]);
         else
            image.append(jjstrLiteralImages[11]);
                      System.out.println("going Table");
         break;
      case 13 :
        if (image == null)
            image = new StringBuffer(jjstrLiteralImages[13]);
         else
            image.append(jjstrLiteralImages[13]);
                             System.out.println("TT");
         break;
      case 18 :
        if (image == null)
            image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
         else
            image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
                                System.out.println("TAC");
         break;
      case 19 :
        if (image == null)
            image = new StringBuffer(jjstrLiteralImages[19]);
         else
            image.append(jjstrLiteralImages[19]);
                         System.out.println("going List");
         break;
      case 26 :
        if (image == null)
            image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
         else
            image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
                               System.out.println("LC");
         break;
      case 27 :
        if (image == null)
            image = new StringBuffer(jjstrLiteralImages[27]);
         else
            image.append(jjstrLiteralImages[27]);
                       System.out.println("going Title");
         break;
      case 33 :
        if (image == null)
            image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
         else
            image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
                                System.out.println("TC");
         break;
      case 40 :
        if (image == null)
            image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
         else
            image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
                          System.out.println("S:" + curLexState);
         break;
      default : 
         break;
   }
}
}
