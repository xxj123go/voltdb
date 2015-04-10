/* This file is part of VoltDB.
 * Copyright (C) 2008-2015 VoltDB Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package org.voltdb.planner;


public class TestFunctions extends PlannerTestCase {

    @Override
    protected void setUp() throws Exception {
        final boolean planForSinglePartitionFalse = false;
        setupSchema(TestFunctions.class.getResource("testplans-functions-ddl.sql"), "testfunctions",
                                                    planForSinglePartitionFalse);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Before fixing ENG-913, the SQL would compile, even though 'user'
     * was not a column name and is a reserved word.
     */
    public void testENG913_userfunction() {
        failToCompile("update ENG913 set name = 'tim' where user = ?;", "'user'", "not supported");
    }

    public void testBitwise() {
        String errorMsg = "incompatible data type in conversion";
        failToCompile("select bitand(tinyint_type, 3) from bit;", errorMsg);
        failToCompile("select bitand(INTEGER_TYPE, 3) from bit;", errorMsg);
        failToCompile("select bitand(FLOAT_TYPE, 3) from bit;", errorMsg);
        failToCompile("select bitand(VARCHAR_TYPE, 3) from bit;", errorMsg);


        failToCompile("select bitor(tinyint_type, 3) from bit;", errorMsg);
        failToCompile("select bitor(INTEGER_TYPE, 3) from bit;", errorMsg);
        failToCompile("select bitor(FLOAT_TYPE, 3) from bit;", errorMsg);
        failToCompile("select bitor(VARCHAR_TYPE, 3) from bit;", errorMsg);


        failToCompile("select bitxor(tinyint_type, 3) from bit;", errorMsg);
        failToCompile("select bitxor(INTEGER_TYPE, 3) from bit;", errorMsg);
        failToCompile("select bitxor(FLOAT_TYPE, 3) from bit;", errorMsg);
        failToCompile("select bitxor(VARCHAR_TYPE, 3) from bit;", errorMsg);


        failToCompile("select bitnot(tinyint_type) from bit;", errorMsg);
        failToCompile("select bitnot(INTEGER_TYPE) from bit;", errorMsg);
        failToCompile("select bitnot(FLOAT_TYPE) from bit;", errorMsg);
        failToCompile("select bitnot(VARCHAR_TYPE) from bit;", errorMsg);

        // out of range exception
        errorMsg = "numeric value out of range";
        failToCompile("select bitand(bigint_type, 9223372036854775809) from bit;", errorMsg);
        failToCompile("select bitand(bigint_type, -9223372036854775809) from bit;", errorMsg);
    }

}
