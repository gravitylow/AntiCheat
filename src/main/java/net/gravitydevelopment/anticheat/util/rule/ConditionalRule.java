/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012-2013 AntiCheat Team | http://gravitydevelopment.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.gravitydevelopment.anticheat.util.rule;

import net.gravitydevelopment.anticheat.manage.CheckType;
import net.gravitydevelopment.anticheat.manage.User;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.SortedMap;

/**
 * The conditional rule follows the same syntax as the traditional CS conditional operator.<br />
 * (CONDITION) ? (TRUE RESULT) : (FALSE RESULT)<br />
 * Where CONDITION is a statement that will be evaluated as either <b>TRUE</b> or <b>FALSE</b>
 * and the results are actions to be taken for either outcome.
 * <br /><br />
 * <p/>
 * Should you need additional actions, the system will recursively parse the following values and handle appropriately.
 * <br />
 * For instance, (CONDITION) ? (TRUE RESULT) : (CONDITION) ? (TRUE RESULT) : (FALSE RESULT) is a valid rule
 * <br /><br />
 * <p/>
 * An example of a valid Conditional Rule:<br />
 * Check_SPIDER > 0 ? Player.KICK : null<br />
 * <i>The above statement would read 'If the spider check has been failed over zero times, kick the player. Otherwise, do nothing.'</i>
 * <p/>
 * To see syntax for variables and functions that you may use, see {@link net.gravitydevelopment.anticheat.util.rule.Rule}
 */
public class ConditionalRule extends Rule {

    private static ScriptEngineManager factory = new ScriptEngineManager();
    private static ScriptEngine engine = factory.getEngineByName("js");

    private static final String TRUE_DELIMITER = "\\?";
    private static final String FALSE_DELIMITER = ":";
    private static final Type TYPE = Type.CONDITIONAL;

    public ConditionalRule(String string) {
        super(string, TYPE);
    }

    @Override
    public boolean check(User user, CheckType type) {
        try {
            // Load all variables
            SortedMap<String, Object> map = getVariables(user, type);
            for (String key : map.keySet()) {
                engine.put(key, map.get(key));
            }

            boolean value = (Boolean) engine.eval(getString().split(TRUE_DELIMITER)[0]);
            // Yo dawg I heard you like conditionals...
            String next = value ? getString().split(TRUE_DELIMITER)[1].split(FALSE_DELIMITER)[0] : getString().split(TRUE_DELIMITER)[1].split(FALSE_DELIMITER)[1];

            execute(next, user, type);
            return value;
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void execute(String string, User user, CheckType type) {
        // If we're told to do nothing
        if (string.equalsIgnoreCase("null") || string.equalsIgnoreCase("none")) return;

        // If this string is a new conditional statement
        if (TYPE.matches(string)) {
            new ConditionalRule(string).check(user, type);
        } else if (isVariableSet(string)) {
            setVariable(string.split("=")[0], string.split("=")[1], user);
        } else if (isFunction(string)) {
            doFunction(string, type, user);
        }
    }
}
