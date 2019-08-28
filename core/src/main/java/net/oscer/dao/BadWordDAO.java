package net.oscer.dao;

import net.oscer.beans.BadWord;
import net.oscer.common.ApiResult;
import net.oscer.framework.FormatTool;
import net.oscer.framework.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 敏感词列表
 *
 * @author MRCHENIKE
 * @create 2019-08-28 16:33
 **/
public class BadWordDAO extends CommonDao<BadWord> {

    public static final BadWordDAO ME = new BadWordDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 敏感词检测
     *
     * @param content
     * @param tip     是否提示违禁字符
     * @return
     */
    public ApiResult check(String content, boolean tip) {
        if (StringUtils.isEmpty(content)) {
            return ApiResult.success();
        }
        content = FormatTool.text(content);
        content = StringUtils.lowerCase(content).replaceAll(" ", "");
        if (StringUtils.isEmpty(content)) {
            return ApiResult.success();
        }

        List<BadWord> all = (List<BadWord>) BadWord.ME.list();
        if (CollectionUtils.isEmpty(all)) {
            return ApiResult.success();
        }
        for (BadWord badWord : all) {
            if (FormatTool.CheckContent(badWord.getText(), content)) {

                return ApiResult.failWithMessage(tip ? badWord.getText() : "存在违禁文本");
            }
        }
        return ApiResult.success();

    }
}
