package dummy

import com.gemstone.gemfire.pdx.PdxReader
import com.gemstone.gemfire.pdx.PdxSerializer
import com.gemstone.gemfire.pdx.PdxWriter


class DummyPdxSerializer implements PdxSerializer {

    @Override
    public Object fromData(Class<?> raw, PdxReader reader) {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    public boolean toData(Object obj, PdxWriter writer) {
        // TODO Auto-generated method stub
        return false
    }

}
