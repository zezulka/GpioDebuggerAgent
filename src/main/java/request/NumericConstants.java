/*
 * Copyright 2017 Miloslav Zezulka.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package request;

/**
 *
 * @author Miloslav Zezulka
 */
public final class NumericConstants {

    private NumericConstants() {
    }

    /**
     * Maximum slave addressing (10-bit addressing taken into consideration)
     */
    public static final int I2C_MAX_ADDR = 1023;
    public static final int MIN_NUM_ARGS = 3;
}
