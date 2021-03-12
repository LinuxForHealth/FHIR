# file: _plugins/expand_nested_variable_filter.rb

module Jekyll
    module ExpandNestedVariableFilter
      def flatify(input)
        Liquid::Template.parse(input).render(@context)
      end
    end
  end
  
  Liquid::Template.register_filter(Jekyll::ExpandNestedVariableFilter)