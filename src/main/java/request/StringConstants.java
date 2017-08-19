/*
 * Copyright 2017 Miloslav.
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
public enum StringConstants {

    ERROR_RESPONSE("ERR"),
    REQ_SEPARATOR(":"),
    GPIO_RESPONSE_FORMAT("GPIO:%s:%s"),
    I2C_READ_RESPONSE_FORMAT("I2C:%s"),
    I2C_WRITE_RESPONSE_FORMAT("I2C:Write completed successfully"),
    SPI_READ_RESPONSE_FORMAT("SPI:%s"),
    SPI_WRITE_RESPONSE_FORMAT("SPI:Write completed successfully"),
    VAL_SEPARATOR(" ");
    private final String msg;

    StringConstants(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
