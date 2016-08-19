/*
 * Copyright (C) 2015 The Pennsylvania State University and the University of Wisconsin
 * Systems and Internet Infrastructure Security Laboratory
 *
 * Author: Damien Octeau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.psu.cse.siis.ic3.db;

import java.sql.SQLException;

public class IntentCategoryTable extends TwoIntTable {
  IntentCategoryTable() {
    super("IntentCategories", "intent_id", "category");
  }

  public int insert(int intentId, int category) throws SQLException {
    return super.insert(intentId, category);
  }

  public int forceInsert(int intentId, int category) throws SQLException {
    return super.forceInsert(intentId, category);
  }

  public int find(int intentId, int category) throws SQLException {
    return super.find(intentId, category);
  }
}