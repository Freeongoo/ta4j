/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2021 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ta4jexamples.loaders;

import com.opencsv.CSVReader;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import ta4jexamples.utils.DateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Other order of records
 * This class build a Ta4j bar series from a CSV file containing bars.
 */
public class CustomCsvBarsLoader {

    public static BarSeries loadCsvSeries(String seriesName, String filename, String dateFormat) {
        InputStream stream = CustomCsvBarsLoader.class.getClassLoader().getResourceAsStream(filename);
        BarSeries series = new BaseBarSeries(seriesName);

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',', '"',
                1)) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                LocalDateTime localDateTime = DateUtils.parseToLocalDateTime(line[0], dateFormat);
                ZonedDateTime date = DateUtils.asZonedDateTime(localDateTime);

                double open = Double.parseDouble(line[1]);
                double close = Double.parseDouble(line[2]);
                double high = Double.parseDouble(line[3]);
                double low = Double.parseDouble(line[4]);
                double volume = Double.parseDouble(line[5]);

                series.addBar(date, open, high, low, close, volume);
            }
        } catch (IOException ioe) {
            Logger.getLogger(CustomCsvBarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars from CSV", ioe);
        } catch (NumberFormatException nfe) {
            Logger.getLogger(CustomCsvBarsLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
        }
        return series;
    }

    public static BarSeries loadCsvSeries(String seriesName, String filename) {
        return loadCsvSeries(seriesName, filename, "yyyy-MM-dd");
    }
}
